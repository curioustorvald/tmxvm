package net.torvald.tmxvm

import net.torvald.tmxvm.Opcode.*

typealias TMXWord = Int

/**
 * Created by minjaesong on 2022-02-17.
 */
class TMXVM(instSize: Int) {

    // registers are purposefully Public to minimise the delay caused by added get/setters

    // Registers //
    var acc: TMXWord = 0
    var x: TMXWord = 0
    var y: TMXWord = 0
    var p0: TMXWord = 0
    var p1: TMXWord = 0
    var b0: TMXWord = 0
    var b1: TMXWord = 0

    // Internal Registers //
    var _pc = 0 // NOTE: jump instruction assumes 1 to be the first instruction, but in the VM the first instruction is 0.

    var busRead0: () -> TMXWord = { 0 }
    var busRead1: () -> TMXWord = { 0 }
    var busWrite0: (TMXWord) -> Unit = {}
    var busWrite1: (TMXWord) -> Unit = {}

    private val BRKInst = Inst(BRK)

    val instructions = Array(instSize) { BRKInst }

    private var finished = false

    fun halt() {
        finished = true
    }

    fun reset() {
        acc = 0
        x = 0
        y = 0
        p0 = 0
        p1 = 0
        b0 = 0
        b1 = 0
        _pc = 0
        finished = false
    }

    fun purgeInst() {
        instructions.indices.forEach { instructions[it] = BRKInst }
    }

    fun setNewInst(newInst: Array<Instruction>) {
        instructions.indices.forEach { instructions[it] = newInst.getOrElse(it) { BRKInst } }
    }

    fun setNewInstInline(vararg inst: Instruction) {
        setNewInst(inst as Array<Instruction>)
    }

    fun stepExec() {
        if (!finished) instructions[_pc].exec()
    }

    fun printState() = "ACC:$acc\tX:$x\tY:$y\tp0:$p0\tPC:$_pc"

    fun Instruction.exec() {
        when (this.opcode) {
            BRK -> halt()
            NOP -> {}
            JPZ -> { if (acc == 0) _pc = this.arg - 2 }
            JNZ -> { if (acc != 0) _pc = this.arg - 2 }
            JMP -> { _pc = this.arg - 2 }
            INX -> { x += 1 }
            INY -> { y += 1 }
            INC -> { acc += 1 }
            DEX -> { x -= 1 }
            DEY -> { y -= 1 }
            DEC -> { acc -= 1 }
            ADX -> { acc += x }
            ADY -> { acc += y }
            ADD -> { acc += this.arg }
            SBX -> { acc -= x }
            SBY -> { acc -= y }
            SUB -> { acc -= this.arg }
            MUX -> { acc *= x }
            MUY -> { acc *= y }
            MUL -> { acc *= this.arg }
            DVX -> { acc /= x }
            DVY -> { acc /= y }
            DIV -> { acc /= this.arg }
            NOX -> { x = -x-1 }
            NOY -> { y = -y-1 }
            NOT -> { acc = -acc-1 }
            ANX -> { acc = acc and x }
            ANY -> { acc = acc and y }
            AND -> { acc = acc and this.arg }
            ORX -> { acc = acc or x }
            ORY -> { acc = acc or y }
            BOR -> { acc = acc or this.arg }
            XOX -> { acc = acc xor x }
            XOY -> { acc = acc xor y }
            XOR -> { acc = acc xor this.arg }
            SLX -> { acc = acc shl x }
            SLY -> { acc = acc shl y }
            SHL -> { acc = acc shl this.arg }
            SRX -> { acc = acc shr x }
            SRY -> { acc = acc shr y }
            SHR -> { acc = acc shr this.arg }
            WP0 -> { p0 = acc }
            WP1 -> { p1 = acc }
            WB0 -> { b0 = acc; busWrite0(b0) }
            WB1 -> { b1 = acc; busWrite1(b1) }
            WP0I -> { p0 = this.arg }
            WP1I -> { p1 = this.arg }
            WB0I -> { b0 = this.arg; busWrite0(b0) }
            WB1I -> { b1 = this.arg; busWrite1(b1) }
            RP0 -> { acc = p0 }
            RP1 -> { acc = p1 }
            RB0 -> { acc = busRead0() }
            RB1 -> { acc = busRead1() }
            XB0 -> { busRead0() }
            XB1 -> { busRead1() }
            LDX -> { x = this.arg }
            LDY -> { y = this.arg }
            LDA -> { acc = this.arg }
            TXY -> { y = x }
            TXA -> { acc = x }
            TYX -> { x = y }
            TYA -> { acc = y }
            TAX -> { x = acc }
            TAY -> { y = acc }
        }
        _pc += 1
    }

}
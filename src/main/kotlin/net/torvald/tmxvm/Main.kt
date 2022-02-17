package net.torvald.tmxvm

import net.torvald.tmxvm.Opcode.*
import kotlin.math.roundToLong
import kotlin.system.measureNanoTime

fun main(args: Array<String>) {
    val vm = TMXVM(16)

    vm.setNewInstInline(
        Inst(INC),
        Inst(TAX),
        Inst(INX),
        Inst(XOX),
        Inst(JPZ, 1),
        Inst(LDA, 17),
        Inst(TAX),
        Inst(INX),
        Inst(XOX),
        Inst(JMP, 1)
    )

    println("== INSTRUCTION LISTING ==")
    vm.instructions.forEachIndexed { index, instruction ->
        println("${index+1}\t$instruction")
    }

    repeat(10) {
        vm.reset()
        var runCnt = 0
        val runTime = measureNanoTime {
            while (runCnt < 100000000) {
                vm.stepExec()
                runCnt += 1
            }
        }
        println("Exec speed: ${((runCnt.toDouble() / (runTime / 1000000000.0))).roundToLong()} inst/s")
    }
}
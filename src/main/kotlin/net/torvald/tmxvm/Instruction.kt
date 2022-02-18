package net.torvald.tmxvm
import net.torvald.tmxvm.Opcode.*

typealias Instruction = Inst.Instruction

/**
 * Created by minjaesong on 2022-02-17.
 */
object Inst {

    operator fun invoke(opcode: Opcode, arg: TMXWord? = null): Instruction {
        if (opcode.takesArg() && arg == null) throw NullPointerException("No argument given for $opcode")

        if (arg == null || arg == 0)
            return instCache[opcode]!!
        else {
            return Instruction(opcode, arg)
        }
    }

    private val instCache = HashMap<Opcode, Instruction>()

    init {
        instCache[BRK] = Instruction(BRK, 0)
        instCache[NOP] = Instruction(NOP, 0)
        instCache[JPZ] = Instruction(JPZ, 0)
        instCache[JNZ] = Instruction(JNZ, 0)
        instCache[JMP] = Instruction(JMP, 0)
        instCache[INX] = Instruction(INX, 0)
        instCache[INY] = Instruction(INY, 0)
        instCache[INC] = Instruction(INC, 0)
        instCache[DEX] = Instruction(DEX, 0)
        instCache[DEY] = Instruction(DEY, 0)
        instCache[DEC] = Instruction(DEC, 0)
        instCache[ADX] = Instruction(ADX, 0)
        instCache[ADY] = Instruction(ADY, 0)
        instCache[ADD] = Instruction(ADD, 0)
        instCache[SBX] = Instruction(SBX, 0)
        instCache[SBY] = Instruction(SBY, 0)
        instCache[SUB] = Instruction(SUB, 0)
        instCache[MUX] = Instruction(MUX, 0)
        instCache[MUY] = Instruction(MUY, 0)
        instCache[MUL] = Instruction(MUL, 0)
        instCache[DVX] = Instruction(DVX, 0)
        instCache[DVY] = Instruction(DVY, 0)
        instCache[DIV] = Instruction(DIV, 0)
        instCache[NOX] = Instruction(NOX, 0)
        instCache[NOY] = Instruction(NOY, 0)
        instCache[NOT] = Instruction(NOT, 0)
        instCache[ANX] = Instruction(ANX, 0)
        instCache[ANY] = Instruction(ANY, 0)
        instCache[AND] = Instruction(AND, 0)
        instCache[ORX] = Instruction(ORX, 0)
        instCache[ORY] = Instruction(ORY, 0)
        instCache[OR ] = Instruction(OR , 0)
        instCache[XOX] = Instruction(XOX, 0)
        instCache[XOY] = Instruction(XOY, 0)
        instCache[XOR] = Instruction(XOR, 0)
        instCache[SLX] = Instruction(SLX, 0)
        instCache[SLY] = Instruction(SLY, 0)
        instCache[SHL] = Instruction(SHL, 0)
        instCache[SRX] = Instruction(SRX, 0)
        instCache[SRY] = Instruction(SRY, 0)
        instCache[SHR] = Instruction(SHR, 0)
        instCache[WP0] = Instruction(WP0, 0)
        instCache[WP1] = Instruction(WP1, 0)
        instCache[WB0] = Instruction(WB0, 0)
        instCache[WB1] = Instruction(WB1, 0)
        instCache[WP0I] = Instruction(WP0, 0)
        instCache[WP1I] = Instruction(WP1, 0)
        instCache[WB0I] = Instruction(WB0, 0)
        instCache[WB1I] = Instruction(WB1, 0)
        instCache[RP0] = Instruction(RP0, 0)
        instCache[RP1] = Instruction(RP1, 0)
        instCache[RB0] = Instruction(RB0, 0)
        instCache[RB1] = Instruction(RB1, 0)
        instCache[XB0] = Instruction(XB0, 0)
        instCache[XB1] = Instruction(XB1, 0)
        instCache[LDX] = Instruction(LDX, 0)
        instCache[LDY] = Instruction(LDY, 0)
        instCache[LDA] = Instruction(LDA, 0)
        instCache[TXY] = Instruction(TXY, 0)
        instCache[TXA] = Instruction(TXA, 0)
        instCache[TYX] = Instruction(TYX, 0)
        instCache[TYA] = Instruction(TYA, 0)
        instCache[TAX] = Instruction(TAX, 0)
        instCache[TAY] = Instruction(TAY, 0)
    }

    /** Do not call this class directly; use `Inst(Opcode, arg)` instead. */
    class Instruction {

        val opcode: Opcode
        val arg: TMXWord

        internal constructor(opcode: Opcode, arg: TMXWord) {
            this.opcode = opcode
            this.arg = arg
        }

        override fun equals(other: Any?): Boolean {
            return (other as Instruction).let {
                it.opcode == this.opcode && it.arg == this.arg
            }
        }

        override fun toString(): String {
            return if (this.opcode.takesArg())
                "$opcode $arg"
            else
                "$opcode"
        }
    }
}

enum class Opcode {
    BRK, NOP,
    JPZ, JNZ, JMP,
    INX, INY, INC, DEX, DEY, DEC,
    ADX, ADY, ADD, SBX, SBY, SUB,
    MUX, MUY, MUL, DVX, DVY, DIV,
    NOX, NOY, NOT,
    ANX, ANY, AND, ORX, ORY, OR,
    XOX, XOY, XOR,
    SLX, SLY, SHL, SRX, SRY, SHR,
    WP0, WP1, WB0, WB1,
    WP0I, WP1I, WB0I, WB1I,
    RP0, RP1, RB0, RB1,
    XB0, XB1,
    LDX, LDY, LDA,
    TXY, TXA, TYX, TYA, TAX, TAY,
}

fun Opcode.takesArg() = when (this) {
    JPZ, JNZ, JMP, ADD, SUB, MUL, DIV, AND, OR, XOR, SHL, SHR, LDX, LDY, LDA, WP0I, WP1I, WB0I, WB1I, -> true
    else -> false
}
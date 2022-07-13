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
            return instCacheZero[opcode]!!
        else if (arg == 1)
            return instCacheOne[opcode]!!
        else if (arg == -1)
            return instCacheMOne[opcode]!!
        else {
            return Instruction(opcode, arg)
        }
    }

    private val instCacheZero = HashMap<Opcode, Instruction>()
    private val instCacheOne = HashMap<Opcode, Instruction>()
    private val instCacheMOne = HashMap<Opcode, Instruction>()

    init {
        listOf(instCacheMOne, instCacheZero, instCacheOne).forEachIndexed { index, map ->
            val index = index - 1
            map[BRK] = Instruction(BRK, index)
            map[NOP] = Instruction(NOP, index)
            map[JPZ] = Instruction(JPZ, index)
            map[JNZ] = Instruction(JNZ, index)
            map[JMP] = Instruction(JMP, index)
            map[INX] = Instruction(INX, index)
            map[INY] = Instruction(INY, index)
            map[INC] = Instruction(INC, index)
            map[DEX] = Instruction(DEX, index)
            map[DEY] = Instruction(DEY, index)
            map[DEC] = Instruction(DEC, index)
            map[ADX] = Instruction(ADX, index)
            map[ADY] = Instruction(ADY, index)
            map[ADD] = Instruction(ADD, index)
            map[SBX] = Instruction(SBX, index)
            map[SBY] = Instruction(SBY, index)
            map[SUB] = Instruction(SUB, index)
            map[MUX] = Instruction(MUX, index)
            map[MUY] = Instruction(MUY, index)
            map[MUL] = Instruction(MUL, index)
            map[DVX] = Instruction(DVX, index)
            map[DVY] = Instruction(DVY, index)
            map[DIV] = Instruction(DIV, index)
            map[NOX] = Instruction(NOX, index)
            map[NOY] = Instruction(NOY, index)
            map[NOT] = Instruction(NOT, index)
            map[ANX] = Instruction(ANX, index)
            map[ANY] = Instruction(ANY, index)
            map[AND] = Instruction(AND, index)
            map[ORX] = Instruction(ORX, index)
            map[ORY] = Instruction(ORY, index)
            map[BOR] = Instruction(BOR, index)
            map[XOX] = Instruction(XOX, index)
            map[XOY] = Instruction(XOY, index)
            map[XOR] = Instruction(XOR, index)
            map[SLX] = Instruction(SLX, index)
            map[SLY] = Instruction(SLY, index)
            map[SHL] = Instruction(SHL, index)
            map[SRX] = Instruction(SRX, index)
            map[SRY] = Instruction(SRY, index)
            map[SHR] = Instruction(SHR, index)
            map[WP0] = Instruction(WP0, index)
            map[WP1] = Instruction(WP1, index)
            map[WP2] = Instruction(WP2, index)
            map[WP3] = Instruction(WP3, index)
            map[WB0] = Instruction(WB0, index)
            map[WB1] = Instruction(WB1, index)
            map[WB2] = Instruction(WB2, index)
            map[WB3] = Instruction(WB3, index)
            map[WP0I] =Instruction(WP0, index)
            map[WP1I] =Instruction(WP1, index)
            map[WP2I] =Instruction(WP2, index)
            map[WP3I] =Instruction(WP3, index)
            map[WB0I] =Instruction(WB0, index)
            map[WB1I] =Instruction(WB1, index)
            map[WB2I] =Instruction(WB2, index)
            map[WB3I] =Instruction(WB3, index)
            map[RP0] = Instruction(RP0, index)
            map[RP1] = Instruction(RP1, index)
            map[RP2] = Instruction(RP2, index)
            map[RP3] = Instruction(RP3, index)
            map[RB0] = Instruction(RB0, index)
            map[RB1] = Instruction(RB1, index)
            map[RB2] = Instruction(RB2, index)
            map[RB3] = Instruction(RB3, index)
            map[XB0] = Instruction(XB0, index)
            map[XB1] = Instruction(XB1, index)
            map[XB2] = Instruction(XB2, index)
            map[XB3] = Instruction(XB3, index)
            map[LDX] = Instruction(LDX, index)
            map[LDY] = Instruction(LDY, index)
            map[LDA] = Instruction(LDA, index)
            map[TXY] = Instruction(TXY, index)
            map[TXA] = Instruction(TXA, index)
            map[TYX] = Instruction(TYX, index)
            map[TYA] = Instruction(TYA, index)
            map[TAX] = Instruction(TAX, index)
            map[TAY] = Instruction(TAY, index)
        }
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
    ANX, ANY, AND, ORX, ORY, BOR,
    XOX, XOY, XOR,
    SLX, SLY, SHL, SRX, SRY, SHR,
    WP0, WP1, WP2, WP3,
    WB0, WB1, WB2, WB3,
    WP0I, WP1I, WP2I, WP3I,
    WB0I, WB1I, WB2I, WB3I,
    RP0, RP1, RP2, RP3,
    RB0, RB1, RB2, RB3,
    XB0, XB1, XB2, XB3,
    LDX, LDY, LDA,
    TXY, TXA, TYX, TYA, TAX, TAY,
}

fun Opcode.takesArg() = when (this) {
    JPZ, JNZ, JMP, ADD, SUB, MUL, DIV, AND, BOR, XOR, SHL, SHR, LDX, LDY, LDA, WP0I, WP1I, WP2I, WP3I, WB0I, WB1I, WB2I, WB3I -> true
    else -> false
}
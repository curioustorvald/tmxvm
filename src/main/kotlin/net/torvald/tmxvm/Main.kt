package net.torvald.tmxvm

import net.torvald.tmxvm.Opcode.*
import kotlin.math.roundToLong
import kotlin.system.measureNanoTime

fun main(args: Array<String>) {
    val vm = TMXVM(24)

    vm.busRead0 = {
        System.nanoTime().toInt()
    }

    vm.setNewInstInline(
        Inst(RB0), // initial setup; Bus0 has NanosecRTC
        Inst(TAX), // initial setup
        Inst(TXA),
        Inst(MUL, 1103515245),
        Inst(ADD, 12345),
        Inst(TAX),
        Inst(WP0I, 1), // debug key
        Inst(WP0I, 0),
        Inst(TXA),
        Inst(SHR, 5),
        Inst(AND, 1),
        Inst(JPZ, 17),
        Inst(TXA),
        Inst(INC),
        Inst(XOX),
        Inst(JMP, 3),
        Inst(TXA),
        Inst(DEC),
        Inst(XOX),
        Inst(JMP, 3)
    )

    println("== INSTRUCTION LISTING ==")
    vm.instructions.forEachIndexed { index, instruction ->
        println("${index+1}\t$instruction")
    }

    val runSpeeds = ArrayList<Long>()

    repeat(10) {
        vm.reset()
        var runCnt = 0
        val runTime = measureNanoTime {
            while (runCnt < 16777216){//268435456) {
                vm.stepExec()
//                println(vm.printState())
                if (vm.p0 == 1) println(vm.x)
                runCnt += 1
            }
        }
        runSpeeds.add(((runCnt.toDouble() / (runTime / 1000000000.0))).roundToLong())
    }

    println("Run Speeds:")
    runSpeeds.forEachIndexed { index, l -> println("${index+1}\t$l inst/s") }
}
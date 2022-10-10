import io.github.tundraclimate.clib.Result
import org.junit.jupiter.api.Test

class Test {
    @Test
    fun test() {
        Result.ok<String, String>("okok?").then {
            if (it == "okok") Result.err("kore err")
            else Result.ok("okdedayo-")
        }.then {
            println("err nara denai")
            Result.ok("ok")
        }.isSuccess { println("success $it") }.isFailed { println("failed $it") }
    }
}
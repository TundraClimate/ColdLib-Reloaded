package io.github.tundraclimate.clib

class Result<S, E> private constructor(
    private val isOk: Boolean,
    private val success: S?,
    private val err: E?
) {
    companion object {
        @JvmStatic
        fun <S, E> ok(value: S?): Result<S, E> {
            return Result(true, success = value, err = null)
        }

        @JvmStatic
        fun <S, E> err(err: E?): Result<S, E> {
            return Result(false, success = null, err = err)
        }
    }

    fun isSuccess(isSuccess: (value: S?) -> Unit): Result<S, E> {
        if (isOk) isSuccess(success)
        return this
    }

    fun isFailed(isErr: (value: E?) -> Unit): Result<S, E> {
        if (!isOk) isErr(err)
        return this
    }

    fun <T> then(isSuccess: (value: S?) -> Result<T, E>): Result<T, E> {
        return if (isOk) isSuccess(success) else err(err)
    }
}
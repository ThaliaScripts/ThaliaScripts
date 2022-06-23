package dev.bozho.utils

object ReflectionUtil {
    fun invoke(obj: Any, methodName: String): Boolean {
        return try {
            val method = obj.javaClass.getDeclaredMethod(methodName)
            method.isAccessible = true
            method.invoke(obj)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun field(obj: Any, fieldName: String): Any? {
        return try {
            val field = obj.javaClass.getDeclaredField(fieldName)
            field.isAccessible = true
            field.get(obj)
        } catch (e: Exception) {
            null
        }
    }
}
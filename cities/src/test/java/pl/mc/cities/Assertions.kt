package pl.mc.cities

import org.junit.Assert
import kotlin.reflect.KClass

suspend fun <T : Throwable> coAssertFail(
  expected: KClass<T>,
  action: suspend () -> Unit
) {
  try {
    action.invoke()
  } catch (e: Throwable) {
    if (e.javaClass == expected.java) {
      return
    } else Assert.fail("Expected ${expected.java.name} but was ${e.javaClass.name}")
  }
  Assert.fail("Expected ${expected.java.name} but was no error")
}

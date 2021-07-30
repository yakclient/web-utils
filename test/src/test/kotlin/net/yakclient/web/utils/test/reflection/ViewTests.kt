package net.yakclient.web.utils.test.reflection

import net.yakclient.web.utils.annotation.ViewProperty
import net.yakclient.web.utils.annotation.Viewable
import net.yakclient.web.utils.reflection.ViewUtils
import net.yakclient.web.utils.reflection.configure
import net.yakclient.web.utils.test.runSpeedTest
import org.junit.jupiter.api.Test

//@ContextConfiguration
//@SpringBootTest(classes = [Application::class])
class ViewTests {
    @Test
    fun testViewAutoConfig() {
        val value = getData()

        println(ViewUtils.view(value))
    }

    @Test
    fun testSpeedAutoConfig() {
        val value = getData()
        runSpeedTest(1000000) {
            ViewUtils.view(value)
        }
    }

    @Test
    fun testViewConfig() {
        val value = getData()
        val configure = ViewUtils.configure<TestDataClass>()
        configure.node {
            rename(TestDataClass::fingerCount, "Toe count")
                .get(TestDataClass::favoriteChild).exclude(SuperClass::hairColor)
                .up().getC(TestDataClass::allChildren)
                .rename(ChildDataClass::hairColor, "The other childrens hair color(not favorite)")
        }

        println(configure.view(value))
    }

    private fun getData() =
        TestDataClass(null,
            10,
            "Hot dogs",
            ChildDataClass("Davie", "Blonde"),
            listOf(ChildDataClass("Glen", "Brown"), ChildDataClass("Angela", "red"), null))
}


@Viewable
class TestDataClass(
    @ViewProperty(name = "Not name", ignore = false)
    val name: String?,
    @ViewProperty(ignore = false)
    val fingerCount: Int,
    val favoriteFood: String,
    val favoriteChild: ChildDataClass,
    @ViewProperty(name = "Some children")
    val allChildren: List<ChildDataClass?>,
)

@Viewable
open class SuperClass(
    @ViewProperty(name = "Cabello color")
    val hairColor: String,
)

class ChildDataClass(
    @ViewProperty(ignore = true)
    val childName: String,
    hairColor: String,
) : SuperClass(hairColor)
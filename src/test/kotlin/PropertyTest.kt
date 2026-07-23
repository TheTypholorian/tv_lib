import net.typho.tv_lib.io.impl.PropertiesFileFormat

object PropertyTest {
    @JvmStatic
    fun main(args: Array<String>) {
        val format = PropertiesFileFormat()
        val testText = """
            abc=def
             ghi=jkl
            jkl = mno 
            mno = p\
                q\
                r
            pqr=s\nt\nv
        """
        val output = format.read(testText)
        println(format.write(output))
    }
}
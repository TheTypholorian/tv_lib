import net.typho.tv_lib.io.impl.PropertyFileFormat

object Test {
    @JvmStatic
    fun main(args: Array<String>) {
        val format = PropertyFileFormat()
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
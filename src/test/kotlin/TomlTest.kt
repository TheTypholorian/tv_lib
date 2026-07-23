import net.typho.tv_lib.io.impl.TomlFileFormat

object TomlTest {
    @JvmStatic
    fun main(args: Array<String>) {
        val format = TomlFileFormat()
        val testText = """
            abc="d#ef"
            jhi = 123 # test comment
            klm = '''
            # test \ncomment #2
            '''
        """
        val output = format.read(testText)
        println(output)
    }
}
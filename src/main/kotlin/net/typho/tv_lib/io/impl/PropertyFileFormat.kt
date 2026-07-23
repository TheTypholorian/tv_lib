package net.typho.tv_lib.io.impl

import net.typho.tv_lib.io.DataFileFormat
import net.typho.tv_lib.io.DataObjectSerializer
import net.typho.tv_lib.io.DataObjectSerializer.Companion.flatten
import net.typho.tv_lib.io.DataWithComment
import net.typho.tv_lib.io.FileFormatException

class PropertyFileFormat(
    @JvmField
    val writeComments: Boolean = true,
    @JvmField
    val delimiter: Char = '='
) : DataFileFormat {
    override val extension: String
        get() = "properties"
    override val serializers = mutableListOf<DataObjectSerializer<Any>>()

    override fun read(input: String): Map<String, String> {
        val map = mutableMapOf<String, String>()
        var index = 0

        val lines = input.split('\n').iterator()

        while (lines.hasNext()) {
            val currentIndex = index++
            var line = lines.next().trimStart()

            if (line.isBlank() || line.startsWith('!') || line.startsWith('#')) {
                continue
            }

            line = line.replace("\\\\", "\\")

            while (line.endsWith('\\')) {
                index++
                line = line.substring(0, line.length - 1) + lines.next().trimStart().replace("\\\\", "\\")
            }

            val tokens = line.split('=', ':', limit = 2)

            if (tokens.size < 2) {
                throw FileFormatException("Line $currentIndex is missing a '=' or ':' delimiter: '$line'")
            }

            map[tokens.first().trimEnd()] = tokens.last().trimStart().replace("\\n", "\n").replace("\\r", "\r").replace("\\t", "\t")
        }

        return map
    }

    override fun write(data: Any): String {
        val data = serializers.flatten(data) ?: return ""

        if (data !is Map<*, *>) {
            throw IllegalArgumentException("Property file format requires a Map input, got ${data.javaClass.name}")
        }

        val builder = StringBuilder()

        fun checkComment(value: Any?): Any? {
            return if (value is DataWithComment) {
                if (writeComments) {
                    builder.appendLine("#${value.comment}")
                }

                value.value
            } else value
        }

        data.forEach { (key, value) ->
            val key = checkComment(key) ?: throw NullPointerException("Property file keys must not be null")
            val value = checkComment(value) ?: "null"

            if (key !is CharSequence) {
                throw IllegalArgumentException("Expected a CharSequence key, got ${key.javaClass.name}")
            }

            if (!value.javaClass.isPrimitive && value !is CharSequence) {
                throw IllegalArgumentException("Expected a CharSequence or primitive value, got ${value.javaClass.name}")
            }

            builder.appendLine("$key $delimiter ${value.toString().replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t")}")
        }

        return builder.toString()
    }
}
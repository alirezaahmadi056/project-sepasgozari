package com.viranika.sepasgozari

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class HelperConvert {
    companion object {
        fun convertStreamToString(inStream: InputStream): String {
            val reader = BufferedReader(InputStreamReader(inStream))
            var line: String
            val sb = StringBuilder()
            try {

                do {
                    line = reader.readLine()
                    sb.append(line)
                } while (true)

            } catch (ex: Exception) {

            }
            return sb.toString()
        }
    }
}
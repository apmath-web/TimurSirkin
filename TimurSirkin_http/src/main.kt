package com.stackoverflow.q3732109

import java.io.IOException
import java.io.OutputStream
import java.net.InetSocketAddress
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpServer
import java.net.URLEncoder

object Test {

    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val server = HttpServer.create(InetSocketAddress(8000), 0)
        server.createContext("/date", getDateHandler())
        server.createContext("/wait", waitForHandler())
        server.executor = null
        server.start()
    }

    internal class getDateHandler : HttpHandler {
        @Throws(IOException::class)
        override fun handle(t: HttpExchange) {
            val response = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)
            t.sendResponseHeaders(200, response.length.toLong())
            val os = t.responseBody
            os.write(response.toByteArray())
            os.close()
        }
    }

   internal class waitForHandler : HttpHandler {
        @Throws(IOException::class)
        override fun handle(t: HttpExchange) {

            val sleepTime = t.requestURI.query.toString().split("=")[1]
            Thread.sleep(sleepTime.toLong())
            val response = "Sleep for $sleepTime ms complited"

            val os = t.responseBody

            t.sendResponseHeaders(200, response.length.toLong())
            os.write(response.toByteArray())
            os.close()
        }
    }
}
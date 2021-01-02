package com.nedellis

import zio.console._

object App {

  def foo(x: Array[String]) = x.foldLeft("")((a, b) => a + b)

  def bar(y: String): String = y

  def main(args: Array[String]) {
    println("Hello World!")
    println(s"${bar("Hello World")}")
    println("concat arguments = " + foo(args))
  }

}

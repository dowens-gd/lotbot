package com.technophobia.creep.crawlr

object Crawlr extends App {
  Console.println("Welcome to Crawlr")
  for (seedUrl <- args) {
    Console.println("Indexing from " + seedUrl)
  }  
  Console.println("Bye!")
}
name := "TwitServRevAuthProxy"

version := "1.0"

scalaVersion := "2.11.7"

resolvers += "twttr" at "http://maven.twttr.com/"

libraryDependencies += "com.twitter" %% "twitter-server" % "1.11.0"
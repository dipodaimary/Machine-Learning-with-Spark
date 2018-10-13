//creating rdds
val collection = List("a","b","c","d","e")
val rddFromCollection = sc.parallelize(collection)
//rdd from text file
val rddFromTextFile = sc.textFile("README")

//most common spark transformation is map, 
collection.map(x=>x.toString)
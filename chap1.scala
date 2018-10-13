//creating rdds
val collection = List("a","b","c","d","e")
val rddFromCollection = sc.parallelize(collection)
//rdd from text file
val rddFromTextFile = sc.textFile("README")

//most common spark transformation is map, 
collection.map(x=>x.toString)

//cache data into memory use persist to have fine grain control over how to cache data
collection.cache

//Broadcast variables and accumulators
val broadcastAList = sc.broadcast(List("a", "b", "c", "d", "e"))
broadcastAList.value

sc.parallelize(List("1","2","3")).map(x=>broadcastAList.value ++ x).collect //one of the best one-liners
//accumulators are like broadcast variables but can be added to


val data = sc.textFile("data/UserPurchaseHistory.csv").map(line=>line.split(",")).map(x=>(x(0),x(1),x(2)))
data.map{case (x,y,z)=>x}.distinct.count
//find the most popular product
data.map{case(user,item,amount)=> (item,1)}.reduceByKey(_+_).collect().sortBy(-_._2) //best one-liners

val user_data = spark.textFile("/tmp/ml-100k/u.user")

//Processing and transforming data:

db.persons.aggregate([
	{ $project: { _id: 0, name: 1, email: 1, location: { type: "Point", coordinates: [
		{ $convert: { input: "$location.coordinates.longitude", to: "double", onError: 0.0, onNUll: 0.0} },
		{ $convert: { input: "$location.coordinates.latitude", to: "double", onError: 0.0, onNUll: 0.0} }
		] } }},

	{ $project: 
		{ gender: 1, email: 1, location: 1, fullName: 

			{ $concat: [

				{ $toUpper: 
					{ $substrCP: ["$name.first", 0, 1] } 
				},

				{ $substrCP: [
					"$name.first",
					1, 
					{ $subtract: [{$strLenCP: "$name.first"}, 1] }
					]
				},

				" ", 

				{ $toUpper: 
					{ $substrCP: ["$name.last", 0, 1] }
				},

				{ $substrCP: [
					"$name.last",
					1,
					{ $subtract: [{$strLenCP: "$name.last"}, 1] }
					]
				}

				] 
			} 
		} 
	}
]).pretty()




db.persons.aggregate([
	{
		$project:
		{
			birthdate: 
			{
				$toDate: "$dob.date"
			}
		}
	}
]).pretty()





db.friends.aggregate([
	{ $group: {
		_id: { age: "$age"},
		allHobbies: { $push: "$hobbies"}
	}}
]).pretty()


db.friends.aggregate([
	{ $project: {
		_id: 0, numScores: 
		{ $size: "$examScores"}
	}}
])









db.friends.aggregate([
	{ $project: {
		_id: 0,
		scores:
			{ $filter: { input: "$examScores", as: "sc", cond:
				{ $gt: ["$$sc.score", 60]}	
			}}
	}}
]).pretty()









db.friends.aggregate([
	{ $unwind: "$examScores" },
	{ $project: { _id: 1, name: 1, age: 1, score: "$examScores.score" }},
	{ $sort: { score: -1 }},
	{ $group: { _id: "$_id" , name: { $first: "$name" }, maxScore: { $max: "$score" }}},
	{ $sort: { maxScore: -1}}
]).pretty()





db.persons.aggregate([
	{ $bucket:{
		groupBy: "$dob.age",
		boundaries: [0, 18, 30, 50, 80, 120],
		output: {
			numPersons: { $sum: 1},
			averageAge: { $avg: "dob.age"}
		}
	}}
]).pretty()

db.persons.aggregate([
	{ $bucketAuto: {
		groupBy: "$dob.age",
		buckets: 5,
		output: {
			numPersons: { $sum: 1},
			averageAge: { $avg: "$dob.age"}
		}
	}}
]).pretty()

//不装了



db.persons.aggregate([
	{ $project:
		{
			_id: 0,
			name: 1,
			birthdate: { $toDate: "dob.date"}
		}}
])




//practice task

//to see what cand_offices are there in the dataset
db.science.aggregate([
	{ $project:{
		_id: 0,
		cand_office: 1
	}},
	{ $group:{
		_id:{  },
		all_cand_offices: { $addToSet:"$cand_office" }
	}}
])


//list all states with cand_office == 'H'
db.science.aggregate([
	{ $project:{
		_id: 0,
		cand_office: 1,
		state: 1
	}},
	{ $group:{
		_id:{  },
		all_states: { $addToSet:"$state"}
	}}
])


//not work
db.science.aggregate([
	{ $match: { cand_office: "H" } },
	{ $project:{
		_id: 0,
		state: 1,
		zip_code: 1,
		transaction_amt: 1
	}},

	{ $group: {
		_id: { state: "$state" },
	}},

	{ $bucket: {
		groupBy: "$zip_code",
		boundaries: [0, 50001],
		default: "higherThan50K",
		output:{
			"count": { $sum: 1}
		}
	}}
])



//work, except the bucket
db.science.aggregate([
	{ $match: { cand_office: "H" } },
	{ $group: {
		_id: { state: "$state" },
		info: { $push:{
			zip_code: "$zip_code",
			transaction_amt: "$transaction_amt"
		}}
	}}
]).pretty()


//put everything back together
db.science.aggregate([
	{ $project:{
		_id: 0,
		cand_office: 1,
		state: 1,
		amt_zip_less_50k: { $cond: [{ $lte: ["$zip_code", 50000]}, "$transaction_amt", 0]},
		amt_zip_more_50k: { $cond: [{ $gt: ["$zip_code", 50000]}, "$transaction_amt", 0]}
	}},
	{ $match: { cand_office: "H" } },
	{ $group: {
		_id: { state: "$state" },
		sum_of_zip_lte_50k: { $sum: "$amt_zip_less_50k"},
		sum_of_zip_gt_50k: { $sum: "$amt_zip_more_50k"}
	}},
	{ $addFields:{
		amt_difference: { $subtract: ["$sum_of_zip_lte_50k", "$sum_of_zip_gt_50k"]}
	}}
]).pretty()









//SonarW

db.policy_violations.aggregate(
	{ $project:{
		OS_User_that_violated_rule_3:
			{ $query:{$ns: "policy_violations", $q:{ "Access Rule Description": "Violation Rule #3" }, $p:"OS User"}}
	}}
).pretty()



db.session.aggregate(
	{ $project:{
		ids_with_server_type_H2:
			{ $query: {$ns: "session", $q:{ "Server Type": "H2"}, $p: "_id"}}
	}}
).pretty()


// Find out how many server types in the collection
db.session.aggregate(
	{ $group:{
		_id:{
			server_type: "$Server Type"
		}
	}
})


//Find out how many LOgin Succeed types
db.session.aggregate(
	{ $group:{
		_id:{
			login_succeeded_type: "$Login Succeeded"
		}
	}
})

//q2 time!
db.session.aggregate([
	{ $match:{
		"Login Succeeded": NumberLong(0)
	}},
	{ $sort:{"Session Start": 1}},
	{ $group:{
		_id: {
			server_type: "$Server Type"
		},
		db_user_name: { $first: "$DB User Name"},
		session_start_time: { $first: "$Session Start"}
	}},
	{ $project:{
		db_user_name: "$db_user_name",
		session_start_time: { $dateToString: {
			date: "$session_start_time",
			format: "%m-%d-%H-%M"
		}}
	}}
])

//now is the q1!!
db.full_sql.aggregate([
	{ $join: {
		$joined: {
			dim1: "session"
		},
		$match: {
			"Session Id": "$dim1.$_id"
		},
		$project: {
			db_user_name: "$DB User Name",
			Timestamp: 1,
			Succeeded: 1,
			min: { $add: [{ $min: "$dim1.$Session Start"}, 1000*60]},
			max: { $add: [{ $min: "$dim1.$Session Start"}, 2000*60]}
		}
	}},
	{ $project: {
		db_user_name: 1,
		Timestamp: 1,
		Succeeded: 1,
		min:1,
		max:1,
		min_sub: { $subtract: [ "$Timestamp", "$min" ]},
		max_sub: { $subtract: [ "$max", "$Timestamp" ]}
	}},
	{ $match: {
		Succeeded: NumberLong(0),
		min_sub: { $gte: NumberLong(0)},
		max_sub: { $gte: NumberLong(0)}
	}},
	{ $group: {
		_id: {db_user_name: "$db_user_name"},
		count_of_fail_sqls: { $sum: 1}
	}}
]).pretty()


db.full_sql.aggregate([
	{ $project: {
		session_start_time: { $query: {
			$ns: "session",
			$p: "Session Start",
			$a: [
				{ $sort: {"$Session Start": 1}},
				{ $limit: 1}
			]
		}},
		Timestamp: 1,
		db_user_name: "$DB User Name",
		Succeeded: 1
	}},
	{ $project: {
		Timestamp: 1,
		db_user_name: 1,
		Succeeded: 1,
		min: { $add: [{ $min: "$session_start_time"}, 1000*60]},
		max: { $add: [{ $min: "$session_start_time"}, 2000*60]}
	}},
	{ $project: {
		db_user_name:1,
		Timestamp:1,
		Succeeded: 1,
		min:1,
		max:1,
		min_sub: { $subtract: [ "$Timestamp", "$min" ]},
		max_sub: { $subtract: [ "$max", "$Timestamp" ]}
	}},
	{ $match: {
		Succeeded: NumberLong(0),
		min_sub: { $gte: NumberLong(0)},
		max_sub: { $gte: NumberLong(0)}
	}},
	{ $group: {
		_id: {db_user_name: "$db_user_name"},
		count_of_fail_sqls: { $sum: 1}
	}}
]).pretty()

//The two methods above gives different results, because of the $join stage.












//Test case by Yair
db.session.aggregate([
    { $project: {
		"Session Start": 1,
		"UTC Offset": 1,
		"DB User Name": 1
	}},
	
	{ $out: {
		"name": "out_collection",
		"fstype": "local",
		"format" : "json",
		formattingOptions: "splunk"
	}}
])




db.test.aggregate([
    { $project: {
		Name: 1,
		Age: 1,
		"Register Time": 1,
		Notes: 1
	}},
	
	{ $out: {
		name: "outfile",
		fstype: "local",
		format: "json",
		formattingOptions: "splunk"
	}}
])


db.test.aggregate([
    { $project: {
		"*": 1
	}},
	
	{ $out: {
		format: "json",
		formattingOptions: "splunk",
		fstype: "syslog",
		syslog_params:{
			sendto: "qi.local:12345"
		}
	}}
])






db.test.insertOne({
	"name": "foo",
	"date": "idk"
})

db.test.updateOne({ "name": "bar"}, 
    { "$currentDate": { "date": { "$type": date }}}
)

db.test.updateOne({ "name": "bar" }, { "$set": { "date": new Date() }})


















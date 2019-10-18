//This is the javascript script for testing $out on SonarW

db = db.getSiblingDB('outSplunkTest');

db.test.insertOne({
    "ISO Date": new ISODate(),
    "Date": Date(),
    "a": [
        {
            "b": NumberInt(1),
            "c": {
                "sub": false
            }
        },
        {
            "d": "e",
            "f": true
        },
        1000
    ]
});

//db.test.updateOne({},{ "$set": { "time": new Date() }});
db.test.aggregate([
    { $project: {
		"*": 1
	}},	
	{ $out: {
		format: "json",
		formattingOptions: "splunk",
		fstype: "syslog",
		syslog_params:{
            //sendto: "major.local:12345"
            sendto: "main.local:10564",
            protocol:'tcp'
		}
	}}
]);

db.test.drop();

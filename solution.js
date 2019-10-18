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
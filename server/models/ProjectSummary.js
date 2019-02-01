const mongoose = require("mongoose");

const projectSummarySchema = new mongoose.Schema({
	groupId: String,
	artifactId: String
}, {collection: "project_summaries"});

module.exports = mongoose.model("ProjectSummary", projectSummarySchema);
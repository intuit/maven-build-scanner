const express = require("express");
const fs = require("fs");
const mongoose = require("mongoose");
const router = express.Router();

require("../models/ProjectSummary");
require("../models/SessionSummary");
require("../models/SessionProfile");

const ProjectSummary = mongoose.model("ProjectSummary");
const SessionSummary = mongoose.model("SessionSummary");
const SessionProfile = mongoose.model("SessionProfile");

const MONGO_DB_HOST = process.env.MONGO_DB_HOST || "localhost"
const MONGO_DB_PORT = process.env.MONGO_DB_PORT || "27017"
const MongoServerUrl = MONGO_DB_HOST + ":" + MONGO_DB_PORT;

mongoose.connect("mongodb://"+MongoServerUrl+"/build_scans", { useNewUrlParser: true });
mongoose.Promise = global.Promise;

router.get("/", (req, res) => {
	fs.readFile("index.html", (error, content) => res.write(content));

});

router.get("/api/v1/project-summaries", (req, res) => {
	ProjectSummary.find()
		.then((projectSummaries) => {
			const out = {};
			projectSummaries.forEach(projectSummary => {
				out[projectSummary.id] = projectSummary;
			});
			res.json(out);
		})
		.catch(() => {
			res.status(500);
			res.send();
		});

});
router.get("/api/v1/session-summaries/:projectId", (req, res) => {
	const projectId = req.params.projectId;
	const groupId = projectId.replace(/:.*/, "");
	const artifactId = projectId.replace(/.*:/, "");
	SessionSummary.find({"project.groupId": groupId, "project.artifactId": artifactId})
		.then((sessionSummaries) => {
			const out = {};
			sessionSummaries.forEach(sessionSummary => {
				out[sessionSummary.id] = sessionSummary;
			});
			res.json(out);
		})
		.catch(() => {
			res.status(500);
			res.send();
		});

});
router.get("/api/v1/session-profiles/:projectId/:sessionId", (req, res) => {
	const projectId = req.params.projectId;
	const sessionId = req.params.sessionId;
	const groupId = projectId.replace(/:.*/, "");
	const artifactId = projectId.replace(/.*:/, "");
	SessionProfile.findOne({"project.groupId": groupId, "project.artifactId": artifactId, "id": sessionId})
		.then((sessionProfile) => {
			res.json(sessionProfile);
		})
		.catch(() => {
			res.status(500);
			res.send();
		});

});
module.exports = router;

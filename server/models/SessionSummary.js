const mongoose = require('mongoose');

const sessionSummarySchema = new mongoose.Schema({
    id: String,
    project: {
        groupId: String,
        artifactId: String
    },
}, {collection: "session_summaries"});

module.exports = mongoose.model('SessionSummary', sessionSummarySchema);
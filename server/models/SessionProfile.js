const mongoose = require('mongoose');

const sessionProfileSchema = new mongoose.Schema({
    id: String,
    project: {
        groupId: String,
        artifactId: String
    },
}, {collection: "session_profiles"});

module.exports = mongoose.model('SessionProfile', sessionProfileSchema);
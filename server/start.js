require('./models/ProjectSummary');
require('./models/SessionSummary');
require('./models/SessionProfile');
const app = require('./app');

const server = app.listen(3000, () => {
    console.log(`Express is running on port ${server.address().port}`);
});

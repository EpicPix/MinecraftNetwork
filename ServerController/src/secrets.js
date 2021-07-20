class Secrets {

    constructor(mongo) {
        this.mongo = mongo;
    }

    getConnectionString() {
        return `mongodb${this.mongo.srv?'+srv':''}://${encodeURIComponent(this.mongo.username)}:${encodeURIComponent(this.mongo.password)}@${this.mongo.host}`;
    }

}

module.exports = { Secrets };
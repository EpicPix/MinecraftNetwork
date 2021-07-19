module ga.epicpix.network.common {

    requires org.mongodb.bson;
    requires org.mongodb.driver.core;
    requires org.mongodb.driver.sync.client;
    requires com.google.gson;

    exports ga.epicpix.network.common;
    exports ga.epicpix.network.common.commands;
    exports ga.epicpix.network.common.servers;

}
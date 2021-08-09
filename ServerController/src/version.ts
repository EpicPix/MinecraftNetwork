export const versionList: ServerVersion[] = [];

export class ServerVersion {
    constructor(private protocolVersion: number, private stringVersion: string) {}

    public getProtocolVersion(): number {
        return this.protocolVersion;
    }

    public getStringVersion(): string {
        return this.stringVersion;
    }

}

export function addVersion(protocol: number, version: string): ServerVersion {
    var ver = new ServerVersion(protocol, version);
    versionList.push(ver);
    return ver;
}

export function addVersionList(protocol: number, versions: string[]) {
    for(const ver of versions) {
        addVersion(protocol, ver);
    } 
}

addVersionList(47,  ["1.8", "1.8.1", "1.8.2", "1.8.3", "1.8.4", "1.8.5", "1.8.6", "1.8.7", "1.8.8"]);
addVersion    (107,  "1.9");
addVersion    (108,  "1.9.1");
addVersion    (109,  "1.9.2");
addVersionList(110, ["1.9.3", "1.9.4"]);
addVersionList(210, ["1.10", "1.10.1", "1.10.2"]);
addVersion    (315,  "1.11");
addVersionList(316, ["1.11.1", "1.11.2"]);
addVersion    (335,  "1.12");
addVersion    (338,  "1.12.1");
addVersion    (340,  "1.12.2");
addVersion    (393,  "1.13");
addVersion    (401,  "1.13.1");
addVersion    (404,  "1.13.2");
addVersion    (477,  "1.14");
addVersion    (480,  "1.14.1");
addVersion    (485,  "1.14.2");
addVersion    (490,  "1.14.3");
addVersion    (498,  "1.14.4");
addVersion    (573,  "1.15");
addVersion    (575,  "1.15.1");
addVersion    (578,  "1.15.2");
addVersion    (735,  "1.16");
addVersion    (736,  "1.16.1");
addVersion    (751,  "1.16.2");
addVersion    (753,  "1.16.3");
addVersionList(754, ["1.16.4", "1.16.5"]);
addVersion    (755,  "1.17");
addVersion    (756,  "1.17.1");
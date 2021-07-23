package ga.epicpix.network.common.websocket;

public enum Capability {

    CAPSRVSIG(0x0001),
    CAPSETTINGUPD(0x0002);

    private final int msk;

    Capability(int msk) {
        this.msk = msk;
    }

    public int getBits() {
        return msk;
    }

}

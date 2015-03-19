package ServerClients.UDPpackets;

import ServerClients.Client;
import ServerClients.Server;



public abstract class UDPPacket {

    public static enum PacketTypes {
        INVALID(-1), WIN(-3),LOGIN(00), DISCONNECT(01), DATA(02),MOVE(03), TELEPORT(04), OPENDOOR(05), PICKUP(06), DROP(07),PICKUPKEY(-2);

        private int UPDpacketId;

        private PacketTypes(int packetId) {
            this.UPDpacketId = packetId;
        }

        public int getId() {
            return UPDpacketId;
        }
    }

    public byte packetId;

    public UDPPacket(int packetId) {
        this.packetId = (byte) packetId;
    }

    public abstract void writeData(Client client);

    public abstract void writeData(Server server);

    public String readData(byte[] data) {
        String message = new String(data).trim();
        return message.substring(2);
    }

    public abstract byte[] getData();
    public abstract byte[] getRealData();

    public static PacketTypes lookupPacket(String packetId) {
        try {
            return lookupPacket(Integer.parseInt(packetId));
        } catch (NumberFormatException e) {
            return PacketTypes.INVALID;
        }
    }

    public static PacketTypes lookupPacket(int id) {
        for (PacketTypes p : PacketTypes.values()) {
            if (p.getId() == id) {
                return p;
            }
        }
        return PacketTypes.INVALID;
    }
}

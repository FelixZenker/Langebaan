class Boss {

    private boolean beaten;

    private Room location;
    private String name;

    public Boss(Room newLocation, String newName) {
        this.location = newLocation;
        this.name = newName;
        this.beaten = false;
    }

    public Room getLocation() {
        return this.location;
    }

    public void geschlagen() {
        this.beaten = true;
    }

    public boolean getGeschlagen() {
        return this.beaten;
    }

}

class Boss {

    private boolean beaten;

    private Room location;
    private String name;

    public Boss(String newName) {
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

    public void setLocation(Room loc)
    {
        if(loc != null && this.location != loc)
        {
            this.location = loc;
        }
        else
        {
            System.out.println("Leider ist ein Problem aufgetreten.");
        }
    }

    public String getName()
    {
        return this.name; 
    }
}

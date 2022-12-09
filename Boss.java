class Boss {

    private boolean beaten = false; 

    private Room location;
    private String name; 
    
    public Boss(Room newLocation, String newName) {
        this.location = newLocation;
        this.name = newName;
    }

}

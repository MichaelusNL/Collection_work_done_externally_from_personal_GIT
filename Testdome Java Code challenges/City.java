import java.util.*;

public class City {
    private Collection<City> adjacentCities;
    private String name;

    public City(String name) {
        this.name = name;
        this.adjacentCities = new ArrayList<City>();
    }

    /**
     * Name of the city, can be assumed to be unique
     */
    public String getName() {
        return name;
    }

    /**
     * Adjacent city are connected to this city directly by road
     */
    public Collection<City> getAdjacentCities() {
        return adjacentCities;
    }

    public void addAdjacentCity(City city) {
        adjacentCities.add(city);
        city.getAdjacentCities().add(this);
    }

    public boolean canDriveTo(City city) {
        List adjacentcities = new ArrayList(adjacentCities);
        for (int i=0;i<adjacentcities.size();i++){
            adjacentcities.get(i);
        }

        return true;
    }

    public static void main(String[] args) {
        City a = new City("A");
        City b = new City("B");
        City c = new City("C");
        City d = new City("D");

        a.addAdjacentCity(b);
        a.addAdjacentCity(c);
        b.addAdjacentCity(c);
        b.addAdjacentCity(d);

        System.out.println(a.canDriveTo(c));
    }
}
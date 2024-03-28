package com.strategygame.frontlines1950.map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.strategygame.frontlines1950.utils.DataManager;
import com.strategygame.frontlines1950.utils.Triple;

import java.util.*;

import static com.strategygame.frontlines1950.Frontlines1950.WORLD_HEIGHT;
import static com.strategygame.frontlines1950.Frontlines1950.WORLD_WIDTH;
public class World {
    private final DataManager dataManager;
    private final Set<Country> countries = new HashSet<>();
    private final Map<Color, Province> provinceByColorMap = new HashMap<>();
    private Texture waterTexture;
    private Country selectedCountry;
    private State selectedState;

    public World() {
        this.dataManager = new DataManager();
        long startTimeLoadCountries = System.currentTimeMillis();
        this.loadCountries();
        long endTimeLoadCountries = System.currentTimeMillis();
        System.out.println("Load countries: " + (endTimeLoadCountries - startTimeLoadCountries) / 1000 + "sec");
        long startTimeLoadStatesAndProvinces = System.currentTimeMillis();
        this.loadStatesAndProvinces();
        long endTimeLoadStatesAndProvinces = System.currentTimeMillis();
        System.out.println("Load states and provinces: " + (endTimeLoadStatesAndProvinces - startTimeLoadStatesAndProvinces) / 1000 + "sec");
        long startTimeInitCountries = System.currentTimeMillis();
        this.initCountries();
        long endTimeInitCountries = System.currentTimeMillis();
        System.out.println("Init countries: " + (endTimeInitCountries - startTimeInitCountries) / 1000 + "sec");
        long startTimeCreateWaterTexture = System.currentTimeMillis();
        this.createWaterTexture();
        long endTimeCreateWaterTexture = System.currentTimeMillis();
        System.out.println("Create water texture: " + (endTimeCreateWaterTexture - startTimeCreateWaterTexture) / 1000 + "sec");
    }

    public void loadCountries() {
        this.countries.addAll(this.dataManager.readCountriesJSON());
    }

    public Set<Country> getCountries() {
        return this.countries;
    }

    public void loadStatesAndProvinces() {
        Map<String, Set<Province>> provincesByCountryIdAndState = this.dataManager.readStatesJSON();
        addStatesAndProvinces(provincesByCountryIdAndState);

        Set<Triple<Integer, String, Color>> provincesInfo = this.dataManager.readDefinitionCSV();
        provincesInfo.forEach(this::addProvinceInfo);

        List<Triple<Color, Integer, Integer>> pixels = this.dataManager.readProvinceBitmap();
        pixels.forEach(this::addPixels);
    }

    public void addStatesAndProvinces(Map<String, Set<Province>> provincesByCountryIdAndState) {
        for (String ids : provincesByCountryIdAndState.keySet()) {
            String[] tabId = ids.split("_");
            for (Country country : this.countries) {
                if (country.getId().equals(tabId[0])) {
                    State state = country.addState(Integer.parseInt(tabId[1]));
                    state.addProvinces(provincesByCountryIdAndState.get(ids));
                }
            }
        }
    }

    public void addProvinceInfo(Triple<Integer, String, Color> info) {
        for (Country country : this.countries) {
            for (State state : country.getStates()) {
                for (Province province : state.getProvinces()) {
                    if (province.getId() == info.getValue0()) {
                        province.setName(info.getValue1());
                        province.setColor(info.getValue2());
                        this.provinceByColorMap.put(province.getColor(), province);
                    }
                }
            }
        }
    }

    public void addPixels(Triple<Color, Integer, Integer> pixel) {
        Province province = this.provinceByColorMap.get(pixel.getValue0());
        if (province != null) {
            province.addPixel(pixel.getValue1(), pixel.getValue2());
        }
    }

    public void render(SpriteBatch batch, float zoom) {
        int i = 0;
        batch.draw(waterTexture, 0, 0);
        batch.draw(waterTexture, WORLD_WIDTH, 0);
        batch.draw(waterTexture, -WORLD_WIDTH, 0);
        for (Country country : this.countries) {
            country.draw(batch, zoom);
        }
        if(this.selectedCountry != null) {
            this.selectedCountry.drawSelected(batch);
        }

        if(this.selectedState != null) {
            this.selectedState.drawSelected(batch);
        }
    }

    public Country selectCountry(int x, int y) {
        for(Country country : this.countries) {
            int adjustedX = x;

            if (x < 0) {
                adjustedX += WORLD_WIDTH;
            } else if (x > WORLD_WIDTH) {
                adjustedX -= WORLD_WIDTH;
            }
            if(country.isPixelCountry(adjustedX, y)) {
                System.out.println(country);
                this.selectedCountry = country;
                return country;
            }
        }

        return null;
    }

    public void deselectCountry() {
        this.selectedCountry = null;
    }

    public void selectState(int x, int y) {
        for (Country country : this.countries) {
            int adjustedX = x;

            if (x < 0) {
                adjustedX += WORLD_WIDTH;
            } else if (x > WORLD_WIDTH) {
                adjustedX -= WORLD_WIDTH;
            }

            if (country.isPixelCountry(adjustedX, y)) {
                for (State state : country.getStates()) {
                    if (state.isPixelState(adjustedX, y)) {
                        this.selectedState = state;
                        System.out.println(selectedState);
                        break;
                    }
                }
            }
        }
    }

    public void deselectState() {
        this.selectedState = null;
    }

    public void initCountries() {
        for(Country country : countries) {
            country.setOrigin();
            country.setDimension();
            for(State state : country.getStates()) {
                state.setOrigin();
                state.setDimension();
                state.setRegiment();
                for(Province province : state.getProvinces()) {
                    province.setOrigin();
                    province.setDimension();
                }
                state.setBorderPixels();
                state.setCenter();
                state.createTexture();
            }
            country.setBorderPixels();
            country.createTexture();
            country.createSelectedTexture();
        }
    }

    public void createWaterTexture() {
        Pixmap pixmap = new Pixmap(WORLD_WIDTH, WORLD_HEIGHT, Pixmap.Format.RGBA8888);
        pixmap.setColor(40 / 255f, 55 / 255f, 80 / 255f, 1f);

        for(int x = 0; x < WORLD_WIDTH; x++) {
            for(int y = 0; y < WORLD_HEIGHT; y++) {
                Color color = new Color(31 / 255f, 43 / 255f, 58 / 255f, 1f);
                pixmap.drawPixel(x, y, Color.rgba8888(color));
            }
        }

        for(Country country : countries) {
            for(Province.Pixel pixel : country.getBorderPixels()) {
                pixmap.fillCircle(pixel.getX(), WORLD_HEIGHT - pixel.getY(), 20);
            }
        }

        this.waterTexture = new Texture(pixmap);
    }

    public void dispose() {
        for(Country country : countries) {
            country.dispose();
            for(State state : country.getStates()) {
                state.dispose();
            }
        }
    }
}

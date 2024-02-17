package com.strategygame.frontlines1950;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.strategygame.frontlines1950.utils.Triple;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

public class DataManager {
    private static final String countriesFileName = "data/countries.json";
    private static final String statesFileName = "data/map/states.json";
    private static final String definitionFileName = "data/map/definition.csv";
    private static final Pixmap bitmap = new Pixmap(Gdx.files.internal("data/map/provinces.bmp"));

    public DataManager() {
    }

    public JsonNode openJson(String fileName) throws JsonProcessingException {
        FileHandle fileHandle = Gdx.files.internal(fileName);

        String contentJson = fileHandle.readString();
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readTree(contentJson);
    }

    public Set<Country> readCountriesJSON() {
        Set<Country> countries = new HashSet<>();
        try {
            JsonNode countriesJson = this.openJson(countriesFileName);

            countriesJson.fields().forEachRemaining(entry -> {
                String countryCode = entry.getKey();
                JsonNode countryNode = entry.getValue();

                String name = countryNode.get("name").asText();
                JsonNode rgbNode = countryNode.get("rgb");
                Country country = new Country(countryCode, name, rgbNode.get(0).asInt(), rgbNode.get(1).asInt(), rgbNode.get(2).asInt());
                countries.add(country);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return countries;
    }

    public Map<String, Set<Province>> readStatesJSON() {
        Map<String, Set<Province>> provincesByCountryIdAndState = new HashMap<>();
        try {
            JsonNode stateJson = this.openJson(statesFileName);

            stateJson.fields().forEachRemaining(entry -> {
                String IdCountryAndState = entry.getKey();
                JsonNode values = entry.getValue();

                Set<Province> provinces = new HashSet<>();
                for (JsonNode value : values) {
                    provinces.add(new Province(value.asInt()));
                }

                provincesByCountryIdAndState.put(IdCountryAndState, provinces);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return provincesByCountryIdAndState;
    }

    public Set<Triple<Integer, String, Color>> readDefinitionCSV() {
        Set<Triple<Integer, String, Color>> provincesInfo = new HashSet<>();
        FileHandle fileHandle = Gdx.files.internal(definitionFileName);
        String csvContent = fileHandle.readString();
        String line;
        String cvsSplitBy = ";";

        try (BufferedReader br = new BufferedReader(new StringReader(csvContent))) {
            while ((line = br.readLine()) != null) {
                String[] values = line.split(cvsSplitBy);
                if (!values[0].isEmpty()) {
                    int id = Integer.parseInt(values[0]);
                    float red = Integer.parseInt(values[1]) / 255.0f;
                    float green = Integer.parseInt(values[2]) / 255.0f;
                    float blue = Integer.parseInt(values[3]) / 255.0f;
                    String name = values[4];

                    Triple<Integer, String, Color> info = new Triple<>(id, name, new Color(red, green, blue, 1));
                    provincesInfo.add(info);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return provincesInfo;
    }

    public List<Triple<Color, Integer, Integer>> readProvinceBitmap() {
        List<Triple<Color, Integer, Integer>> pixels = new ArrayList<>();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = new Color(bitmap.getPixel(x, y));
                pixels.add(new Triple<>(color, x, y));
            }
        }

        return pixels;
    }

}

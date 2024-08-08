package common;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;

import static json.CustomJson.MAPPER;

abstract public class Id {
    protected String id;
    
    protected static String generateRandomId(int length){
        Random random = ThreadLocalRandom.current();
        byte[] randomBytes = new byte[length/2];
        random.nextBytes(randomBytes);
        return Base64.getUrlEncoder().encodeToString(randomBytes);
    }
    protected Id(){}

    protected Id(String id){
        this.id = id;
    }

    public static<T extends Id> LinkedHashSet<T> getIds(String filepath) throws IOException {
        JsonNode idNode = MAPPER.readTree(new File(filepath));
        LinkedHashSet<T> ids = new LinkedHashSet<>();
        for (int i = 0; i < ids.size(); i++) {
            ids.add(MAPPER.treeToValue(idNode.get(i).get("id"), new TypeReference<T>() {}));
        }
        return ids;
    }

    protected static<T extends Id> T getUniqueId(String filepath, Class<T> clazz){
        try{
            LinkedHashSet<T> existingIds = getIds(filepath);
            T candidateId;
            do{
                candidateId = clazz.getDeclaredConstructor().newInstance();
            }while(existingIds.contains(candidateId));
            return candidateId;
        }catch(Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    protected Id(int length){
        id = generateRandomId(length);
    }
    
    @Override
    public String toString(){
        return this.id;
    }

    @Override
    public boolean equals(Object other){
        if(!(other instanceof Id)) return false;
        Id otherId = (Id) other;
        return otherId.id.equals(this.id);
    }

    @Override
    public int hashCode(){
        return this.id.hashCode();
    }

}
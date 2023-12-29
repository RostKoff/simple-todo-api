package net.rostkoff.simpletodoapi.client.mappers;

public interface IMap<From, To> {
    To map(From from);
}

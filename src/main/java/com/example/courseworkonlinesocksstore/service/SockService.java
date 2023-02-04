package com.example.courseworkonlinesocksstore.service;

import com.example.courseworkonlinesocksstore.dto.SockRequest;
import com.example.courseworkonlinesocksstore.exception.InSufficientSockQuantityException;
import com.example.courseworkonlinesocksstore.exception.InvalidSockRequestException;
import com.example.courseworkonlinesocksstore.model.Color;
import com.example.courseworkonlinesocksstore.model.Size;
import com.example.courseworkonlinesocksstore.model.Sock;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SockService {

    private final Map<Sock, Integer> socks = new HashMap<>();


    public void addSock(SockRequest sockRequest) {
        validateRequest(sockRequest);
        Sock sock = mapToSock(sockRequest);
        if (socks.containsKey(sock)) {
            socks.put(sock, socks.get(sock) + sockRequest.getQuantity());
        } else {
            socks.put(sock, sockRequest.getQuantity());
        }
    }

    public void issueSock(SockRequest sockRequest) {
        decreaseSockQuantity(sockRequest);
    }

    public void removeDefectiveSocks(SockRequest sockRequest) {
        decreaseSockQuantity(sockRequest);
    }

    private void decreaseSockQuantity(SockRequest sockRequest) {
        validateRequest(sockRequest);
        Sock sock = mapToSock(sockRequest);
        int sockQuantity = socks.getOrDefault(sock, 0);
        if (sockQuantity >= sockRequest.getQuantity()) {
            socks.put(sock, sockQuantity - sockRequest.getQuantity());
        } else {
            throw new InSufficientSockQuantityException("Носков больше нет");
        }
    }

    public int getSockQuantity(Color color, Size size, Integer cottonMin, Integer cottonMax) {
        int total = 0;
        for (Map.Entry<Sock, Integer> entry : socks.entrySet()) {
            if (color != null && !entry.getKey().getColor().equals(color)) {
                continue;
            }
            if (size != null && !entry.getKey().getSize().equals(size)) {
                continue;
            }
            if (cottonMin != null && entry.getKey().getCottonPercentage() < cottonMin) {
                continue;
            }
            if (cottonMax != null && entry.getKey().getCottonPercentage() > cottonMax) {
                continue;
            }
            total += entry.getValue();
        }
        return total;
    }

    private void validateRequest(SockRequest sockRequest) {
        if (sockRequest.getColor() == null || sockRequest.getSize() == null) {
            throw new InvalidSockRequestException("Все поля должны быть заполнены");
        }

        if (sockRequest.getCottonPercentage() < 0 || sockRequest.getCottonPercentage() > 100) {
            throw new InvalidSockRequestException("Процентное содержание хлопка должно составлять от 0 до 100");
        }

        if (sockRequest.getQuantity() <= 0) {
            throw new InvalidSockRequestException("Количество должно быть больше 0");
        }
    }

    private Sock mapToSock(SockRequest sockRequest) {
        return new Sock(sockRequest.getColor(),
                sockRequest.getSize(),
                sockRequest.getCottonPercentage());
    }

}

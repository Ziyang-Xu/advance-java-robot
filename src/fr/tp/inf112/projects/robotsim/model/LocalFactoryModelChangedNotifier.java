
package fr.tp.inf112.projects.robotsim.model;

import fr.tp.inf112.projects.canvas.controller.Observer;

import java.util.ArrayList;
import java.util.List;

public class LocalFactoryModelChangedNotifier implements FactoryModelChangedNotifier {
    private final List<Observer> observers = new ArrayList<>();

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.modelChanged();
        }
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }
}

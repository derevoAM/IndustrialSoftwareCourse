package SatelliteConstellation.exception;

public class AuthExceptions {
    public static class WrongSatelliteParamException extends RuntimeException {
        public WrongSatelliteParamException() {
            super("Неправильный тип параметров спутника");
        }
    }

    public static class NoExistingFactoryException extends RuntimeException {
        public NoExistingFactoryException() {
            super("Фабрики, реализующий данный тип спутника не существует");
        }
    }
}

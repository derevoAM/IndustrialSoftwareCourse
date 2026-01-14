package zoo;

import java.time.LocalTime;
import java.util.List;

import zoo.animals.Animal;
import zoo.animals.mammals.Lion;
import zoo.animals.mammals.Elephant;
import zoo.animals.birds.Parrot;
import zoo.animals.reptiles.Snake;
import zoo.employees.Veterinarian;
import zoo.employees.Keeper;
import zoo.cages.Enclosure;
import zoo.cages.EnclosureType;
import zoo.care.FeedingService;
import zoo.care.MedicalService;
import zoo.care.ReportingService;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== СИСТЕМА УПРАВЛЕНИЯ МОСКОВСКИМ ЗООПАРКОМ ===\n");

        Zoo zoo = new Zoo("Московский зоопарк", "Москва");

        FeedingService feedingService = new FeedingService();
        MedicalService medicalService = new MedicalService();
        ReportingService reportingService = new ReportingService();

        Enclosure mammalEnclosure = new Enclosure("Вольер-1", EnclosureType.MAMMAL_ENCLOSURE);
        Enclosure aviaryEnclosure = new Enclosure("Птичник-1", EnclosureType.BIRD_AVIARY);
        Enclosure terrarium = new Enclosure("Террариум-1", EnclosureType.REPTILE_TERRARIUM);

        zoo.addEnclosure(mammalEnclosure);
        zoo.addEnclosure(aviaryEnclosure);
        zoo.addEnclosure(terrarium);

        Lion simba = new Lion("Симба", 5, 190.0, "Золотистый", 12, 3);
        Elephant dumbo = new Elephant("Дамбо", 8, 5000.0, "Серый", 15, 2);
        Parrot rio = new Parrot("Рио", 3, 1.2, 0.5, "Разноцветный", 0.5, 2);
        Snake petya = new Snake("Петя", 6, 5, "Ребристая", 2, 1);

        zoo.addAnimal(simba);
        zoo.addAnimal(dumbo);
        zoo.addAnimal(rio);
        zoo.addAnimal(petya);

        mammalEnclosure.addAnimal(simba);
        mammalEnclosure.addAnimal(dumbo);
        aviaryEnclosure.addAnimal(rio);
        terrarium.addAnimal(petya);


        System.out.println("\n=== СОЗДАНИЕ РАСПИСАНИЯ КОРМЛЕНИЯ И МЕДКАРТЫ ДЛЯ ЖИВОТНЫХ === ");
        for (Animal animal : zoo.getAnimals()) {
            feedingService.createFeedingSchedule(animal);
            medicalService.registerAnimal(animal);
        }

        System.out.println("\n=== НАЙМ ПЕРСОНАЛА === ");
        Keeper keeper = new Keeper("Иванов", "Млекопитающие");
        Veterinarian vet = new Veterinarian("Сидоров", "Крупные млекопитающие");

        zoo.addEmployee(keeper);
        zoo.addEmployee(vet);

        keeper.assignEnclosure(mammalEnclosure);
        keeper.assignEnclosure(aviaryEnclosure);

        vet.scheduleCheckup(simba);
        vet.scheduleCheckup(dumbo);


        simba.run();
        rio.fly();

        System.out.println("\n=== ИСПОЛНЕНИЕ ОБЯЗАННОСТЕЙ ===");

        LocalTime morningTime = LocalTime.of(9, 0);
        for(Enclosure enclosure: keeper.getAssignedEnclosures()) {
            List<Animal> toFeedMorning = feedingService.getAnimalsNeedingFeeding(
                    enclosure.getAnimals(),
                    morningTime
            );
            for (Animal animal : toFeedMorning) {
                keeper.feedAnimal(animal);
            }
        }

        if(vet.getScheduledCheckups().contains(simba)) {
            medicalService.recordCheckup(simba, vet.examineAnimal(simba));
        }
        for(Enclosure enclosure: keeper.getAssignedEnclosures()){
            keeper.cleanEnclosure(enclosure);
        }

        reportingService.generateAnimalCountReport(zoo.getAnimals());
        reportingService.generateFeedingReport(feedingService);
        reportingService.generateMedicalReport(medicalService);

    }
}

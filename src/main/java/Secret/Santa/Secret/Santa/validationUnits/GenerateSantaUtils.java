package Secret.Santa.Secret.Santa.validationUnits;

import Secret.Santa.Secret.Santa.exception.SantaValidationException;
import Secret.Santa.Secret.Santa.models.GenerateSanta;
import Secret.Santa.Secret.Santa.models.Group;
import Secret.Santa.Secret.Santa.models.User;
import Secret.Santa.Secret.Santa.repos.IGenerateSantaRepo;
import Secret.Santa.Secret.Santa.repos.IGroupRepo;
import Secret.Santa.Secret.Santa.repos.IUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenerateSantaUtils {

    @Autowired
    private IGroupRepo groupRepository;

    @Autowired
    private IUserRepo userRepository;
    @Autowired
    private IGenerateSantaRepo generateSantaRepo;

    public GenerateSantaUtils(IGroupRepo groupRepository, IUserRepo userRepository, IGenerateSantaRepo generateSantaRepo) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.generateSantaRepo = generateSantaRepo;
    }

    public GenerateSanta getGenerateSantaById(Integer id) {
        return generateSantaRepo.findById(id)
                .orElseThrow(() -> new SantaValidationException("Generate_Santa does not exist", "id",
                        "Generate_Santa not found", String.valueOf(id)));
    }

    public GenerateSanta getBySantaAndGroup(User user, Group group) {
        return generateSantaRepo.findBySantaAndGroup(user, group)
                .orElseThrow(() -> new SantaValidationException("Generate_Santa does not exist", "id",
                        "Generate_Santa not found", String.valueOf(user.getUserId())));
    }

    public GenerateSanta getByUserAndGroup(User user, Group group) {
        return generateSantaRepo.findByRecipientAndGroup(user, group)
                .orElseThrow(() -> new SantaValidationException("Generate_Santa does not exist", "id",
                        "Generate_Santa not found", String.valueOf(user.getUserId())));
//
    }

//    public boolean existsByName(String name) {
//        return generateSantaRepo.existsByNameIgnoreCase(name);
//    }
//
//    public void checkGroupNameUnique(String name) {
//        if (existsByName(name)) {
//            throw new ScheduleValidationException("Group name must be unique",
//                    "name", "Name already exists", name);
//        }
//    }
}

package soloproject.seomoim;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MoimService {

    private final MoimRepository moimRepository;

    public Moim create(Moim moim){
        Moim savedMoim = moimRepository.save(moim);
        return savedMoim;
    }

    public List<Moim> findAll(){
        List<Moim> all = moimRepository.findAll();
        return all;
    }

}


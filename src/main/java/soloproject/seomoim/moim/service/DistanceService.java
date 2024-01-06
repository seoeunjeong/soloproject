package soloproject.seomoim.moim.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.moim.entitiy.Moim;
import soloproject.seomoim.moim.service.MoimService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DistanceService {

    private final MoimService moimService;
    private static final double DIRECTION_KM=10.0;


    /*todo 회원에게 반경안의 모임을 가까운 거리순으로 추천*/
    public List<Moim> findNearbyMoims(Member member) {
        double lat = member.getLatitude();
        double log = member.getLongitude();

        return moimService.findAll().stream()
                .filter(moim -> calculateDistance(lat, log, moim.getLatitude(), moim.getLongitude()) <= DIRECTION_KM)
                .sorted(Comparator.comparingDouble(moim -> calculateDistance(lat, log, moim.getLatitude(), moim.getLongitude())))
                .collect(Collectors.toList());
    }


    //위도와 경도를 이용해 거리를 구하는 메소드
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);

        double earthRadius = 6371;

        return earthRadius * Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));
    }

}

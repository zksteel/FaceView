package org.faceview.gallery;

import org.faceview.gallery.entity.Photo;
import org.faceview.gallery.repository.PhotoRepository;
import org.faceview.gallery.service.PhotoServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class PhotoServiceTests {

    private static final String EXPECTED_PHOTO_URL = "testUrl";

    @Mock
    private PhotoRepository photoRepository;

    @InjectMocks
    private PhotoServiceImpl photoService;

    @Before
    public void startUp() {
        when(this.photoRepository.save(any())).
                thenReturn(new Photo() {{
                               setId("1");
                               setUrl("testUrl");
                               setBlobId("testBlobId");
                           }}
                );
    }

    @Test
    public void testSavePhoto_withGivenPhoto_shouldReturnTheSavePhoto(){

        Photo photo = new Photo();
        photo.setId("1");
        photo.setBlobId("testBlobId");
        photo.setUrl("testUrl");

        Photo result = this.photoService.save(new Photo());

        assertEquals(result.getUrl(), EXPECTED_PHOTO_URL);
    }
}

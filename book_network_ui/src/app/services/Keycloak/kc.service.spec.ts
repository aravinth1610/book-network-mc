import { TestBed } from '@angular/core/testing';
import { KcService } from './kc.service';


describe('KcService', () => {
  let service: KcService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(KcService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

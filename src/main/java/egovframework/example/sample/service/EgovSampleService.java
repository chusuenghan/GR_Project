package egovframework.example.sample.service;

import java.util.List;

public abstract interface EgovSampleService
{
  public abstract String insertSample(SampleVO paramSampleVO)
    throws Exception;
  
  public abstract void updateSample(SampleVO paramSampleVO)
    throws Exception;
  
  public abstract void deleteSample(SampleVO paramSampleVO)
    throws Exception;
  
  public abstract SampleVO selectSample(SampleVO paramSampleVO)
    throws Exception;
  
  public abstract List<?> selectSampleList(SampleDefaultVO paramSampleDefaultVO)
    throws Exception;
  
  public abstract int selectSampleListTotCnt(SampleDefaultVO paramSampleDefaultVO);
}
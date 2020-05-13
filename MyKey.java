public class MyKey
{
    private String nonTerm;
    private String term;

    public MyKey(String a,String b)
    {
      this.nonTerm = a;
      this.term = b;
    }

    @Override
    public int hashCode(){
      return 1;
    }

    @Override
    public boolean equals(Object o)
    {
      MyKey m = (MyKey) o;
      return this.nonTerm.equals(m.nonTerm) && this.term.equals(m.term);
    }

    public String getTerm()
    {
      return this.term;
    }

    public String getNonTerm()
    {
      return this.nonTerm;
    }
}

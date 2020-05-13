public class MyKey
{
    private String nonTerm;
    private String term;

    public MyKey(String a,String b)
    {
      this.nonTerm = a;
      this.term = b;
    }

    public boolean equals(MyKey m)
    {
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

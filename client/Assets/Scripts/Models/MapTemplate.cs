using Assets.Scripts.GraphicCustoms;
using System.Collections.Generic;

namespace Assets.Scripts.Models
{
    public class MapTemplate
    {
        public int id;

        public int type;

        public int planetId;

        public string name;

        public int row;

        public int column;

        public string data;

        public int[,] datas;

        public List<int> imgsBgr = new List<int>();

        public int[,] colorsBgr = new int[4, 3];

        public Image imgBgr;

        public int bgrId;

        public MapTemplate()
        {

        }

        public string GetPlanetName()
        {
            if (planetId == 0)
            {
                return "Trái đất";
            }
            if (planetId == 1)
            {
                return "Namek";
            }
            if (planetId == 2)
            {
                return "Sinh tồn";
            }
            if (planetId == 3)
            {
                return "Lửa";
            }
            if (planetId == 4)
            {
                return "Băng";
            }
            if (planetId == 5)
            {
                return "Yardrat";
            }
            if (planetId == 6)
            {
                return "Bill";
            }
            return "Kì bí";
        }
    }
}

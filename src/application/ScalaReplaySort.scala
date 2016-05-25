package application

class ScalaReplaySort extends Constants {
  private val updateTime = 50000
  private val itGoodGame = 36000;

  // get first sort mass
  def sortRepOne(lastNumber: Int, replayNumber: Array[Int],
      sortCount: Array[Int]):Array[Int] = {
    for (i <- (0).to(lastNumber - 1)) {
      for (j <- (i + 1).to(lastNumber - 1)) {
        if (sortCount(i) > sortCount(j)) {
          val tempOne = sortCount(i)
          val tempTwo = replayNumber(i)
          sortCount(i) = sortCount(j)
          replayNumber(i) = replayNumber(j)
          sortCount(j) = tempOne
          replayNumber(j) = tempTwo
        }
      }
    }
    return replayNumber
  }

  // get second sort mass
  def sortRepTwo(lastNumber: Int, replayNumber: Array[Int],
      sortCount: Array[Int]):Array[Int] = {
    for (i <- (0).to(lastNumber - 1)) {
      for (j <- (i + 1).to(lastNumber - 1)) {
        if (sortCount(i) > sortCount(j)) {
          val tempOne = sortCount(i)
          val tempTwo = replayNumber(i)
          sortCount(i) = sortCount(j)
          replayNumber(i) = replayNumber(j)
          sortCount(j) = tempOne
          replayNumber(j) = tempTwo
        }
      }
    }
    return sortCount
  }

  // get average playing time
  def getAveragePlayingTime(lastNumber: Int, sortCount:
      Array[Int]):Int = {
    var averagePlayingTime = 0
    for (i <- (0).to(lastNumber - 1)) {
      averagePlayingTime += sortCount(i)
    }
    return averagePlayingTime / updateTime
  }

  // get average number of opponents
  def getAverageNumberOfOpponents(lastNumber: Int, sortCount:
      Array[Int]):Int = {
    var averageNumberOfOpponents = 0
    for (i <- (0).to(lastNumber - 1)) {
      averageNumberOfOpponents += sortCount(i)
    }
    return averageNumberOfOpponents * 10 / updateTime
  }

  // get average number of shoots
  def getAverageNumberOfShoots(lastNumber: Int, sortCount:
      Array[Int]):Int = {
    var averageNumberOfShoots = 0
    for (i <- (0).to(lastNumber - 1)) {
      averageNumberOfShoots += sortCount(i)
    }
    return averageNumberOfShoots * 10 / updateTime
  }

  // get good games
  def getGoodGames(lastNumber: Int, sortCount:
      Array[Int]):Int = {
    var goodGames = 0
    for (i <- (0).to(lastNumber - 1)) {
      if (sortCount(i) > itGoodGame) {
        goodGames += 1
      }
    }
    return goodGames
  }

  def replayTranscript(action : Int):String = {
    action match {
      case 0 => "main hero goes down\n"
      case 1 => "main hero goes left\n"
      case 2 => "main hero goes right\n"
      case 3 => "main hero goes up\n"
      case 4 => "create new bot\n"
      case 5 => "main hero make shoot\n"
    }
  }
}


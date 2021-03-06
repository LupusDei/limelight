#- Copyright � 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Waves

  def mouse_entered(e)
    @step = -1
    @animation = animate(:updates_per_second => 30) do
      @step = -1 if style.gradient_penetration == "100%"
      @step = 1 if style.gradient_penetration == "1%"
      style.gradient_penetration = (style.gradient_penetration.to_i + @step).to_s
    end
  end

  def mouse_exited(e)
    @animation.stop
  end
  
end
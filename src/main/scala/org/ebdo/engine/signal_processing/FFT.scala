/** Copyright (C) 2017 Project-EBDO
  *
  * This program is free software: you can redistribute it and/or modify
  * it under the terms of the GNU General Public License as published by
  * the Free Software Foundation, either version 3 of the License, or
  * (at your option) any later version.
  *
  * This program is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  * GNU General Public License for more details.
  *
  * You should have received a copy of the GNU General Public License
  * along with this program.  If not, see <http://www.gnu.org/licenses/>.
  */

package org.ebdo.engine.signal_processing;

import scala.math.{cos,Pi,pow,abs,sqrt}
import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D;

/**
  * Wrapper class over edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D that 
  * computes FFT of nfft size over the signal of length nfft.
  * 
  * Author: Paul Nguyen HD, Alexandre Degurse
  *
  */


class FFT(nfft: Int) {

  // Instantiate the low level class that computes the fft
  val lowLevelFtt: DoubleFFT_1D = new DoubleFFT_1D(nfft)

  /**
  * Function that computes FFT for an Array
  * The segmentation of the signal ensures that signal.length == nfft
  * An IllegalArgumentException is thrown if signal.length != nfft
  * @param signal The signal to process as an Array[Double]
  * @return The FFT over the input signal
  */
  def compute(signal: Array[Double]) : Array[Double] = {
    if (signal.length != nfft) {
      throw new IllegalArgumentException("Incorrect signal length (${signal.length}) for FFT (${nfft})")
    }

    // new value that contains the signal and padded with nfft zeros
    // because the size doubles due to complex values
    val fft: Array[Double] = signal ++ Array.fill(nfft)(0.0);

    // // In place computation
    lowLevelFtt.realForwardFull(fft)

    return fft
  }
}
